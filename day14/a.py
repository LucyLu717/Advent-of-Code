def read_input():
    with open("../inputs/day14.txt") as f:
        info = [
            line.strip().replace(" = ", " ").replace("[", " ").replace("]", "")
            for line in f
        ]
    return info


def apply_mask(mask, val):
    assert len(mask) == len(val)
    for i in range(36):
        if mask[i] != "X":
            val[i] = mask[i]
    return int("".join(val), 2)


def apply_mask_2(mask, pos):
    counter = 0
    assert len(mask) == len(pos)
    for i in range(36):
        if mask[i] != "0":
            pos[i] = mask[i]
            if mask[i] == "X":
                counter += 1
    pos = "".join(pos)
    numbers = [pos]
    while counter > 0:
        counter -= 1
        new_nums = []
        for n in numbers:
            for i in ["0", "1"]:
                new_nums.append(n.replace("X", i, 1))
        numbers = new_nums

    results = []
    for n in numbers:
        results.append(int("".join(n), 2))
    return results


def process(inputs):
    mask = ""
    memory = {}
    for i in inputs:
        insn = i.split(" ")
        if insn[0] == "mask":
            mask = insn[1]
        if insn[0] == "mem":
            pos = int(insn[1])
            val = format(int(insn[2]), "036b")
            val = apply_mask(mask, list(str(val)))
            memory[pos] = val
    print(sum(memory.values()))


def process_2(inputs):
    mask = ""
    memory = {}
    for i in inputs:
        insn = i.split(" ")
        if insn[0] == "mask":
            mask = insn[1]
        if insn[0] == "mem":
            pos = format(int(insn[1]), "036b")
            val = int(insn[2])
            poses = apply_mask_2(mask, list(str(pos)))
            for p in poses:
                memory[p] = val
    print(sum(memory.values()))


inputs = read_input()
process(inputs)
process_2(inputs)

