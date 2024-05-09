package com.withus.withmebe.member.dto.auth.request;

import com.withus.withmebe.member.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;

public record SendAuthSmsRequestDto(
    @NotBlank
    @ValidPhoneNumber
    String receiverPhoneNumber
) {
  public SingleMessageSendingRequest toMessage(
      String textContentTemplate, String senderPhoneNumber, String authCode) {
    Message message = new Message();
    message.setFrom(senderPhoneNumber);
    message.setTo(this.receiverPhoneNumber);
    message.setText(String.format(textContentTemplate, authCode));

    return new SingleMessageSendingRequest(message);
  }
}
