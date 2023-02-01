package com.ketul.resource;

import com.ketul.dto.QRCodeDto;
import com.ketul.entity.QRCode;
import com.ketul.service.QRCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class QRCodeResource {

    @Autowired
    private QRCodeService qRCodeService;

    @GetMapping("/")
    public String getQrForm(Model model) {
        model.addAttribute("qRCodeDto", new QRCodeDto());
        return "qr-form";
    }

    @PostMapping("/create-qr")
    public String createQr(@ModelAttribute("dto") QRCodeDto dto, Model model) throws IOException {
        log.info("QRCodeDto : - " + dto);
        if(dto.getName().trim().isEmpty() && dto.getQrText().trim().isEmpty()) {
            model.addAttribute("nameError", "name can not be empty");
            model.addAttribute("qrTextError", "QR Text can not be empty");
            return "qr-form";
        } else if(dto.getName().trim().isEmpty()) {
            model.addAttribute("nameError", "name can not be empty");
            return "qr-form";
        } else if(dto.getQrText().trim().isEmpty()) {
            model.addAttribute("qrTextError", "QR Text can not be empty");
            return "qr-form";
        }

        QRCode qrCode = qRCodeService.generateQrCode(dto);
        if(qrCode == null)
            return "generate-error";

        log.info("qrcode : - " + qrCode);

        String url = MvcUriComponentsBuilder
                .fromMethodName(QRCodeResource.class, "getQrImage", qrCode.getName().toString()).build().toString();
        model.addAttribute("qr", url);
        return "qr";
    }

    @GetMapping("/qrs/{filename:.+}")
    public ResponseEntity<Resource> getQrImage(@PathVariable String filename) {
        Resource file = qRCodeService.loadQr(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

}
