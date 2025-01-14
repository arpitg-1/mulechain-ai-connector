package org.mule.extension.mulechain.internal.llm;


/**
 * This class represents an extension connection just as example (there is no real connection with anything here c:).
 */
public final class LangchainLLMConnection {

  private final String id;

  public LangchainLLMConnection(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void invalidate() {
    // do something to invalidate this connection!
  }
}
