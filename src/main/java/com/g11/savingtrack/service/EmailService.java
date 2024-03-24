package com.g11.savingtrack.service;

import com.g11.savingtrack.utils.EmailUtils;

public interface EmailService {
    Boolean sendSimpleMail(EmailUtils emailUtils);
    Boolean sendMailWithAttachment(EmailUtils emailUtils);
}
