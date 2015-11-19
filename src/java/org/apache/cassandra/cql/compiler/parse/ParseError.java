

public class ParseError {
  private BaseRecognizer br;
  private RecognitionException re;
  private String[] tokenNames;
  
  public ParseError(BaseRecognizer br, RecognitionException re, String[] tokenNames) {
    this.br = br;
    this.re = re;
    this.tokenNames = tokenNames;
    }
  
  public BaseRecognizer getBaseRecognizer() {
    return br;
  }

  public RecognitionException getRecognitionException() {
    return re;
  }
  
  public String[] getTokenNames() {
    return tokenNames;
  }

  public String getMessage() {
    return br.getErrorHeader(re) + " " + br.getErrorMessage(re, tokenNames);
  }

}
