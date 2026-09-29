package dev.obm.privacy;

import java.nio.file.Path;

/** Public integration seam. The baseline intentionally does not implement the task. */
public final class PrivacyPipeline {
  public static final class Request {
    public final Path input;
    public final Path output;
    public final Path state;
    public final String batch;
    public final int generation;
    public final byte[] key;

    public Request(Path input, Path output, Path state, String batch, int generation, byte[] key) {
      this.input = input;
      this.output = output;
      this.state = state;
      this.batch = batch;
      this.generation = generation;
      this.key = key.clone();
    }
  }

  public void run(Request request) throws Exception {
    throw new UnsupportedOperationException("privacy pipeline not implemented");
  }
}
