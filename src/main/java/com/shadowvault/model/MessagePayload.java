package com.shadowvault.model;

public class MessagePayload {
    
    private final String message;
    private final byte[] messageBytes;
    private final int bitLength;
    private final boolean isEncrypted;
    private final String password;
    
    public MessagePayload(String message) {
        this(message, null, false);
    }
    
    public MessagePayload(String message, String password, boolean isEncrypted) {
        this.message = message;
        this.messageBytes = message.getBytes();
        this.bitLength = messageBytes.length * 8;
        this.password = password;
        this.isEncrypted = isEncrypted;
    }
    
    public String getMessage() { return message; }
    public byte[] getMessageBytes() { return messageBytes; }
    public int getBitLength() { return bitLength; }
    public int getByteLength() { return messageBytes.length; }
    public boolean isEncrypted() { return isEncrypted; }
    public String getPassword() { return password; }
    
    public boolean canFitInImage(int imageWidth, int imageHeight) {
        int maxBits = imageWidth * imageHeight;
        return bitLength <= maxBits;
    }
    
    @Override
    public String toString() {
        return String.format("MessagePayload{length=%d bytes, encrypted=%s}", 
            getByteLength(), isEncrypted);
    }
}