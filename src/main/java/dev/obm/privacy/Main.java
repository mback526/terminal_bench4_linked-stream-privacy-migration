package dev.obm.privacy;

import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Main {
  public static void main(String[] args) {
    try {
      if (args.length == 0 || !"anonymize".equals(args[0])) throw new IllegalArgumentException("expected anonymize");
      Map<String,String> a = new LinkedHashMap<>();
      for (int i=1; i<args.length; i+=2) {
        if (i+1 >= args.length || !args[i].startsWith("--")) throw new IllegalArgumentException("invalid arguments");
        a.put(args[i].substring(2), args[i+1]);
      }
      PrivacyPipeline.Request r = new PrivacyPipeline.Request(
          Paths.get(required(a,"input")), Paths.get(required(a,"output")), Paths.get(required(a,"state")),
          required(a,"batch"), Integer.parseInt(required(a,"generation")), Hex.decodeKey(required(a,"key-hex")));
      new PrivacyPipeline().run(r);
    } catch (Exception e) {
      System.err.println("privacy-publisher: " + e.getClass().getSimpleName() + ": " + safe(e.getMessage()));
      System.exit(2);
    }
  }
  private static String required(Map<String,String> m,String k) { String v=m.get(k); if(v==null||v.trim().isEmpty()) throw new IllegalArgumentException("missing --"+k); return v; }
  private static String safe(String s) { return s == null ? "failure" : s.replaceAll("[\r\n]", " "); }
}
