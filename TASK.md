# Linked-stream privacy migration

Implement the incomplete `PrivacyPipeline` used by the `anonymize` command.

Input is a batch directory containing `people.csv`, `contacts.csv`,
`accounts.jsonl`, `activity.jsonl`, and `policy.properties`. References can be
cyclic. Email and phone aliases identify one natural person after trimming,
Unicode NFKC normalization, and lower-casing email values. Never publish a raw
identifier or a reversible derivative of it.

The public command is:

```text
java -Xmx96m -jar privacy-publisher.jar anonymize \
  --input INPUT --output OUTPUT --state STATE --batch BATCH --generation N \
  --key-hex 64_HEX_CHARACTERS
```

Required behavior:

1. Emit deterministic HMAC-SHA-256 tokens, with domain separation for internal
   identity, public person, account, household, and contact tokens.
2. Preserve every reference edge among released records. If a person cannot be
   released, quarantine the complete connected component containing that person.
3. Within each tenant and UTC event month, released quasi-identifier groups must
   meet `k` and contain at least `l` distinct sensitive diagnoses. Generalize age
   using the policy's ordered bucket widths and ZIP using the ordered retained
   prefix lengths; select the first valid pair in that order.
4. Re-running an identical batch is byte-for-byte stable. Reusing a batch name
   with different inputs, lowering a recorded generation, changing the policy
   for an existing state, or truncating/corrupting state must fail closed.
5. A new generation must not reuse public tokens. Emit `bridge.csv` containing
   only prior-generation and current-generation public tokens for identities
   observed in both generations.
6. Commit state and output atomically. Do not leave plaintext identifiers in
   output, state, logs, or temporary files. Appending a later batch must not
   modify files produced for completed batches.

Output files use UTF-8, LF, canonical field order, and lexicographic ordering by
published token. Amount and activity-count totals for released components must be
preserved. Exit `0` only for a committed release; invalid arguments or unsafe
state exit non-zero without modifying completed output.

Do not change `TASK.md`, the command-line contract, or the 96 MiB heap limit.
Different internal algorithms are valid when all observable behavior holds.
