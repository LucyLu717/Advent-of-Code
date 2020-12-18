def read_input():
    with open("../inputs/day8.txt") as f:
        lines = [parse_line(line.strip()) for line in f]
    return lines


def parse_line(line):
    line = line.split(" ")
    return line[0], int(line[1])


def run(lines):
    seen = set()
    acc = 0
    ind = 0
    success = True
    while ind < len(lines):
        if ind in seen:
            success = False
            break
        seen.add(ind)
        insn = lines[ind]
        op = insn[0]

        if op == "nop":
            ind += 1
        elif op == "jmp":
            ind += insn[1]
        elif op == "acc":
            acc += insn[1]
            ind += 1
    return success, acc


def fix(lines):
    problems = ["nop", "jmp"]
    for i, insn in enumerate(lines):
        new_lines = []
        op = insn[0]
        if op in problems:
            new_lines = lines[:i]
            new_lines.append((problems[problems.index(op) - 1], insn[1]))
            new_lines.extend(lines[i + 1 :])
            success, ans = run(new_lines)
            if success:
                print(ans)
                break
        else:
            continue


def main():
    lines = read_input()
    print(run(lines)[1])
    fix(lines)


if __name__ == "__main__":
    main()
