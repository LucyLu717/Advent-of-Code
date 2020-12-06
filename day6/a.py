import string


def read_input():
    lines = []
    answers = []
    with open("../inputs/day6.txt") as f:
        for line in f:
            line = line.strip()
            if len(line) < 1:
                lines.append(answers)
                answers = []
            else:
                answers.append(line)
    return lines


def process_group(answers, op, init):
    yes = init
    for a in answers:
        yes = op(yes, set(list(a)))
    return len(yes)


def process_groups(lines):
    op1, init1 = lambda x, y: x.union(y), set()
    op2, init2 = lambda x, y: x.intersection(y), set(string.ascii_lowercase)
    return (
        sum([process_group(answers, op1, init1) for answers in lines]),
        sum([process_group(answers, op2, init2) for answers in lines]),
    )


def main():
    lines = read_input()
    print(process_groups(lines))


if __name__ == "__main__":
    main()
