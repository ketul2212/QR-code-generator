package com.ketul.service;

import com.ketul.dto.QRCodeDto;
import com.ketul.entity.QRCode;
import org.springframework.core.io.Resource;


public interface QRCodeService {
    QRCode generateQrCode(QRCodeDto qrCodeDto);

    Resource loadQr(String filename);
}
