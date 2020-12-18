import copy

NUMBER = 19690720


def main():
    with open("input.txt") as file:
        row = file.readline()
        numbers = row.split(",")
        for i in range(len(numbers)):
            numbers[i] = int(numbers[i])
        for noun in range(100):
            for verb in range(100):
                pointer = 0
                arr = copy.deepcopy(numbers);
                arr[1] = noun
                arr[2] = verb
                while pointer < len(arr):
                    op = arr[pointer]
                    rs1 = arr[pointer + 1]
                    rs2 = arr[pointer + 2]
                    rd = arr[pointer + 3]
                    if op == 1:
                        arr[rd] = arr[rs1] + arr[rs2]
                    elif op == 2:
                        arr[rd] = arr[rs1] * arr[rs2]
                    else:
                        break
                    pointer += 4
                if arr[0] == NUMBER:
                    print(100 * noun + verb)
                    exit(0)


if __name__ == '__main__':
    main()