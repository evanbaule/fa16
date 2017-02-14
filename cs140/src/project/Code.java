package project;

public class Code {
	public static final int CODE_MAX = 2048;
	int[] code = new int[CODE_MAX];

	public int getOp(int i) {
		return code[2 * i];
	}

	public int getArg(int idx) {
		return code[2 * idx + 1];
	}

	public void clear(int start, int end) {
		int s2 = 2 * start;
		int e2 = 2 * end;

		for (int i = s2; i < e2; i++) {
			code[i] = 0;
		}
	}

	public void setCode(int i, int op, int arg) {
		code[2 * i] = op;
		code[2 * i + 1] = arg;
	}

	public String getHex(int i) {
		return Integer.toHexString(code[2 * i]).toUpperCase() + " " + Integer.toHexString(code[2 * i + 1]);
	}

	public String getDecimal(int i) {
		return InstructionMap.mnemonics.get(code[2 * i]) + " " + code[2 * i + 1];
	}

	public String getText(int i) {
		String s1 = Integer.toHexString(code[2 * i]).toUpperCase();
		String s2 = Integer.toHexString(code[2 * i + 1]).toUpperCase();
		if (code[2 * i + 1] < 0) {
			s2 = "-" + Integer.toHexString(-code[2 * i + 1]);
		}
		return s1 + " " + s2;
	}

}
