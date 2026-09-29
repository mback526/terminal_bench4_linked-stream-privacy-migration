package dev.obm.privacy;

final class Hex {
  static byte[] decodeKey(String s) {
    if (!s.matches("[0-9a-fA-F]{64}")) throw new IllegalArgumentException("key must be 32-byte hex");
    byte[] out=new byte[32];
    for(int i=0;i<out.length;i++) out[i]=(byte)Integer.parseInt(s.substring(i*2,i*2+2),16);
    return out;
  }
}
