

/**
 * Exception from the CQL Parser
 */

import java.util.ArrayList;

public class ParseException extends Exception {

    private static final long serialVersionUID = 1L;
    ArrayList<ParseError> errors = null;

    public ParseException(ArrayList<ParseError> errors)
    {
      super();
      this.errors = errors;
    }

    public ParseException(String message)
    {
        super(message);
    }

    public String getMessage() {

      if (errors == null)
          return super.getMessage();

      StringBuilder sb = new StringBuilder();
      for(ParseError err: errors) {
        sb.append(err.getMessage());
        sb.append("\n");
      }

      return sb.toString();
    }

}
