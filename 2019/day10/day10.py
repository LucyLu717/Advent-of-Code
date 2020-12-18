import math


def main():
    map = []
    with open("input.txt") as file:
        row = file.readline().rstrip()
        width = len(row)
        while row is not None and len(row) > 0:
            map.append(row)
            row = file.readline().rstrip()
    detect = part_one(map, width)
    x, y = find_index(map, detect)
    part_two(map, width, x, y)


def find_index(map, detect):
    max_detect = [0] * len(map)
    max_col = [0] * len(map)
    for i in range(len(detect)):
        max_detect[i] = max(detect[i])
        max_col[i] = detect[i].index(max(detect[i]))
    print(max(max_detect))
    index = max_detect.index(max(max_detect))
    print(index, max_col[index])
    return index, max_col[index]


def part_two(map, width, x, y):
    count = 0
    while count < 200:
        visited = []
        for i in range(len(map)):
            visited.append([0] * width)

        # upper right
        list = []
        for h in range(x + 1):
            for w in range(width - y):
                if h == 0 and w == 0:
                    continue
                list.append((h, w))
        list.sort(key=lambda x: -x[0] / x[1] if x[1] != 0 else -math.pow(2, 20))

        for h, w in list:
            x_cor = x - h
            y_cor = y + w
            while x_cor >= 0 and y_cor < width:
                if map[x_cor][y_cor] == '#':
                    if visited[x_cor][y_cor]:
                        break
                    else:
                        count += 1
                        if count == 200:
                            print(x_cor, y_cor)
                        map[x_cor] = map[x_cor][:y_cor] + "." + map[x_cor][y_cor + 1:]
                        break
                x_cor -= h
                y_cor += w
            while x_cor >= 0 and y_cor < width:
                visited[x_cor][y_cor] = 1
                x_cor -= h
                y_cor += w

        # lower right
        list = []
        for h in range(x + 1):
            for w in range(width - y):
                if h == 0 and w == 0:
                    continue
                list.append((h, w))
        list.sort(key=lambda x: x[0] / x[1] if x[1] != 0 else math.pow(2, 20))

        for h, w in list:
            if h == 0 and w == 0:
                continue
            x_cor = x + h
            y_cor = y + w
            while x_cor < len(map) and y_cor < width:
                if map[x_cor][y_cor] == '#':
                    if visited[x_cor][y_cor]:
                        break
                    else:
                        count += 1
                        if count == 200:
                            print(x_cor, y_cor)
                        map[x_cor] = map[x_cor][:y_cor] + "." + map[x_cor][y_cor + 1:]
                        break
                x_cor += h
                y_cor += w

            while x_cor < len(map) and y_cor < width:
                visited[x_cor][y_cor] = 1
                x_cor += h
                y_cor += w

        # lower left
        list = []
        for h in range(len(map) - x):
            for w in range(y + 1):
                if h == 0 and w == 0:
                    continue
                list.append((h, w))
        list.sort(key=lambda x: -x[0] / x[1] if x[1] != 0 else -math.pow(2, 20))

        for h, w in list:
            if h == 0 and w == 0:
                continue
            x_cor = x + h
            y_cor = y - w
            while x_cor < len(map) and y_cor >= 0:
                if map[x_cor][y_cor] == '#':
                    if visited[x_cor][y_cor]:
                        break
                    else:
                        count += 1
                        if count == 200:
                            print(x_cor, y_cor)
                        map[x_cor] = map[x_cor][:y_cor] + "." + map[x_cor][y_cor + 1:]
                        break
                x_cor += h
                y_cor -= w

            while x_cor < len(map) and y_cor >= 0:
                visited[x_cor][y_cor] = 1
                x_cor += h
                y_cor -= w

        # upper left
        list = []
        for h in range(x + 1):
            for w in range(y + 1):
                if h == 0 and w == 0:
                    continue
                list.append((h, w))
        list.sort(key=lambda x: x[0] / x[1] if x[1] != 0 else math.pow(2, 20))

        for h, w in list:
            x_cor = x - h
            y_cor = y - w
            while x_cor >= 0 and y_cor >= 0:
                if map[x_cor][y_cor] == '#':
                    if visited[x_cor][y_cor]:
                        break
                    else:
                        count += 1
                        if count == 200:
                            print(x_cor, y_cor)
                        map[x_cor] = map[x_cor][:y_cor] + "." + map[x_cor][y_cor + 1:]
                        break
                x_cor -= h
                y_cor -= w
            while x_cor >= 0 and y_cor >= 0:
                visited[x_cor][y_cor] = 1
                x_cor -= h
                y_cor -= w


def part_one(map, width):
    detect =[]
    for i in range(len(map)):
        detect.append([0] * width)
    for x in range(len(map)):
        for y in range(width):
            if map[x][y] == ".":
                continue
            visited = []
            for i in range(len(map)):
                visited.append([0] * width)

            # upper left
            for h in range(x + 1):
                for w in range(y + 1):
                    if h == 0 and w == 0:
                        continue
                    x_cor = x - h
                    y_cor = y - w
                    while x_cor >= 0 and y_cor >= 0:
                        if map[x_cor][y_cor] == '#':
                            if visited[x_cor][y_cor]:
                                break
                            else:
                                detect[x][y] += 1
                                break
                        x_cor -= h
                        y_cor -= w
                    while x_cor >= 0 and y_cor >= 0:
                        if map[x_cor][y_cor] == '#':
                            visited[x_cor][y_cor] = 1
                        x_cor -= h
                        y_cor -= w

            # lower right
            for h in range(len(map) - x):
                for w in range(width - y):
                    if h == 0 and w == 0:
                        continue
                    x_cor = x + h
                    y_cor = y + w
                    while x_cor < len(map) and y_cor < width:
                        if map[x_cor][y_cor] == '#':
                            if visited[x_cor][y_cor]:
                                break
                            else:
                                detect[x][y] += 1
                                break
                        x_cor += h
                        y_cor += w
                    while x_cor < len(map) and y_cor < width:
                        if map[x_cor][y_cor] == '#':
                            visited[x_cor][y_cor] = 1
                        x_cor += h
                        y_cor += w

            # lower left
            for h in range(len(map) - x):
                for w in range(y + 1):
                    if h == 0 and w == 0:
                        continue
                    x_cor = x + h
                    y_cor = y - w
                    while x_cor < len(map) and y_cor >= 0:
                        if map[x_cor][y_cor] == '#':
                            if visited[x_cor][y_cor]:
                                break
                            else:
                                detect[x][y] += 1
                                break
                        x_cor += h
                        y_cor -= w
                    while x_cor < len(map) and y_cor >= 0:
                        if map[x_cor][y_cor] == '#':
                            visited[x_cor][y_cor] = 1
                        x_cor += h
                        y_cor -= w
            # print(x, y, detect)

            # upper right
            for h in range(x + 1):
                for w in range(width - y):
                    if h == 0 and w == 0:
                        continue
                    x_cor = x - h
                    y_cor = y + w
                    while x_cor >= 0 and y_cor < width:
                        if map[x_cor][y_cor] == '#':
                            if visited[x_cor][y_cor]:
                                break
                            else:
                                detect[x][y] += 1
                                break
                        x_cor -= h
                        y_cor += w
                    while x_cor >= 0 and y_cor < width:
                        if map[x_cor][y_cor] == '#':
                            visited[x_cor][y_cor] = 1
                        x_cor -= h
                        y_cor += w

    return detect


if __name__ == '__main__':
    main()