package com.gfgtech.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.ServiceUnavailableException;
import java.util.Set;

/**
 * Helper class for sending email
 */
@Slf4j
public class EmailSender {

    /**
     * Sends an email with {@code content} to {@code addresses}
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void sendMail(String content, Set<String> addresses) throws ServiceUnavailableException {
        log.info("Email with content: '{}' Was sent to {}", content, addresses);
    }
}
