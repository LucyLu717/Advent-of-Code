WIDTH = 25
HEIGHT = 6
TOTAL = WIDTH * HEIGHT


def main():
    with open("input.txt") as file:
        input = file.readline().rstrip()
        layers = part_one(input)
        part_two(layers)


def part_one(input):
    start = 0
    end = TOTAL
    layers = []
    for layer in range(len(input) // TOTAL):
        layers.append(input[start:end])
        start = end
        end = end + TOTAL

    min_layer = min(layers, key=lambda x: x.count("0"))
    print(min_layer.count("1") * min_layer.count("2"))
    return layers


def part_two(layers):
    for h in range(HEIGHT):
        row = ""
        for w in range(WIDTH):
            for layer in layers:
                element = layer[h * WIDTH + w]
                if element == "2":
                    continue
                else:
                    row += element
                    break
        print(row)


if __name__ == '__main__':
    main()