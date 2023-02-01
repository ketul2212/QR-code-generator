package com.ketul.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class QRCodeDto {
    private String name;
    private String qrText;
}
