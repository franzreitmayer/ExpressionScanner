
public class ExpressionScanner {

	private char[] buffer = null;
	
	private Integer pos = 0, start_pos = 0, state = 1, start_state = 1;
	
	public final Integer
		PLUS 		= 1000,
		TIMES		= 1001,
		BRAC_OPEN	= 1002,
		BRAC_CLOSE	= 1003,
		ID			= 1004,
		INT			= 1005;
	
	public ExpressionScanner(String expressionText) {
		this.buffer = expressionText.toCharArray();
	}
	
	private char nextChar() {
		if (pos < buffer.length) {
			return buffer[pos++];
		} else {
			return 0x0;
		}
	}
	
	private void stepback() { pos--; }

	public int nextToken() {
		start_pos = pos;
		start_state = 1; state = 1;
		if (pos >= buffer.length - 1) return -1;
		char c;
		while (true) {
			switch (state) {
			case 1:
				if (nextChar() == '+') state = 2; else state = nextDiagram(); break;
			case 2: return PLUS;
			case 3: if (nextChar() == '*') state = 4; else state = nextDiagram(); break;
			case 4: return TIMES;
			case 5: if (nextChar() == '(') state = 6; else state = nextDiagram(); break;
			case 6: return BRAC_OPEN;
			case 7: if (nextChar() == ')') state = 8; else state = nextDiagram(); break;
			case 8: return BRAC_CLOSE;
			case 9: if ((c = nextChar()) >= 'a' && c <= 'z') state = 10; else state = nextDiagram(); break;
			case 10: if ((c = nextChar()) >= 'a' && c <= 'z') state = 10; else state = 11; break;
			case 11: stepback(); return ID;
			case 12: if ((c = nextChar()) >= '1' && c <= '9') state = 13; else return -1; break;
			case 13: if ((c = nextChar()) >= '0' && c <= '9') state = 13; else state = 14; break;
			case 14: stepback(); return INT;
			default: return -1;
			}
		}
	}
	
	private int nextDiagram() {
		// reset position, because nextDiagramm is called each time the preceding
		// diagram failed, hence we've to give a further chance...
		pos = start_pos; // recover saved position
		switch (start_state) {
		case 1: 	start_state = 3; break;
		case 3:		start_state = 5; break;
		case 5:		start_state = 7; break;
		case 7:		start_state = 9; break;
		case 9:		start_state = 12; break;
		}
		return start_state;
	}
	
	public static void main(String[] args) {
		ExpressionScanner scanner = new ExpressionScanner("(52+4)*3+ab");
		int token = 0;
		while((token = scanner.nextToken()) != - 1) {
			System.out.println(token);
		}
	}
}
