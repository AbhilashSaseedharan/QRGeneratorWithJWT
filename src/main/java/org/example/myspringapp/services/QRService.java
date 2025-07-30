package org.example.myspringapp.services;

import org.example.myspringapp.models.AppUser;
import org.example.myspringapp.models.QR;
import org.example.myspringapp.models.QrRequest;
import org.example.myspringapp.repository.AppUserRepository;
import org.example.myspringapp.utils.QrGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class QRService {
    private static final Logger logger = LoggerFactory.getLogger(QRService.class);
    @Autowired
    private AppUserRepository appUserRepository;

    public BufferedImage generateQrImage(String qrText) throws Exception {
        logger.info("Generating QR image for text: {}", qrText);
        return QrGeneratorUtil.generateQrImage(qrText);
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            logger.debug("Current authenticated username: {}", authentication.getName());
            return authentication.getName();
        }
        logger.warn("No authenticated user found");
        return null;
    }

    @Transactional
    public byte[] generateQR(QrRequest qrRequest) throws Exception {
        logger.info("Generating QR for request: {}", qrRequest);
        BufferedImage qrImage = generateQrImage(qrRequest.getLink());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "JPEG", baos);
        // get current user from security context
        String currentUsername = getCurrentUsername();
        AppUser user = appUserRepository.findByUsername(currentUsername);
        if (user != null) {
            logger.info("Associating QR with user: {}", currentUsername);
            QR qr = new QR();
            qr.setName(qrRequest.getName());
            qr.setLink(qrRequest.getLink());
            qr.setAppUser(user);
            user.getQrs().add(qr);
            appUserRepository.save(user);
        } else {
            logger.warn("No user found for username: {}", currentUsername);
        }
        return baos.toByteArray();
    }

    public List<QR> listAllQrs() {
        String currentUsername = getCurrentUsername();
        logger.info("Listing all QRs for user: {}", currentUsername);
        AppUser user = appUserRepository.findByUsername(currentUsername);
        if (user != null) {
            logger.info("Found {} QRs for user: {}", user.getQrs().size(), currentUsername);
            return user.getQrs();
        }
        logger.warn("No QRs found for user: {}", currentUsername);
        return new ArrayList<>();
    }
}
