package com.ctoutweb.argenDePoche.infra.adapter.secondaryAdapter;

import com.ctoutweb.argentDePoche.application.spi.QrCodeProvider;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayOutputStream;

@Component
public class QrCodeProviderAdapter implements QrCodeProvider {

  @Override
  public Mono<byte[]> generate(String url, int imageHeightAndWidth) {
    return Mono.fromCallable(() -> {

      var width = imageHeightAndWidth;
      var height = imageHeightAndWidth;

      QRCodeWriter writer = new QRCodeWriter();
      BitMatrix matrix = writer.encode(url, BarcodeFormat.QR_CODE, width, height);

      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      MatrixToImageWriter.writeToStream(matrix, "PNG", stream);

      return stream.toByteArray();

    }).subscribeOn(Schedulers.boundedElastic());
  }
}
