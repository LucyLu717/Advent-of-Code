BASE = "no other"
MY_BAG = "shiny gold"


def read_input():
    with open("../inputs/day7.txt") as f:
        lines = [line.strip() for line in f]
    return lines


def process_line(line):
    line = line.split("bags contain")
    outer = line[0].strip()
    bags = [b.strip().split(" ") for b in line[1].split(",")]
    inner = {}

    for b in bags:
        if len(b) < 4:
            inner[BASE] = 0
        else:
            inner[" ".join(b[1:3])] = int(b[0])
    return outer, inner


def find_shiny_gold(color_dict):
    colors = set()
    for c in color_dict.keys():
        if c not in colors:
            inner = set(color_dict[c].keys())
            if MY_BAG in inner:
                colors.add(c)
                continue
            while inner != set():
                new_inner = set()
                for i in inner:
                    new_inner.update(color_dict[i].keys())
                if MY_BAG in new_inner:
                    colors.add(c)
                    break
                inner = new_inner
    print(len(colors))


def find_num_bags(color_dict, color):
    level = color_dict[color]
    num = 0
    for color, n in level.items():
        num += n + n * find_num_bags(color_dict, color)
    return num


def main():
    lines = read_input()
    color_dict = {}
    for l in lines:
        outer, inner = process_line(l)
        color_dict[outer] = inner
    color_dict[BASE] = {}
    find_shiny_gold(color_dict)
    print(find_num_bags(color_dict, MY_BAG))


if __name__ == "__main__":
    main()
