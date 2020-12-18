#include <stdio.h>

void partOne(FILE *file)
{
    char line[256];
    int total = 0;
    while (fgets(line, sizeof(line), file))
    {

        int fuel = atoi(line);
        total += fuel / 3 - 2;
    }
    printf("%d \n", total);
}

void partTwo(FILE *file)
{
    char line[256];
    int total = 0;

    while (fgets(line, sizeof(line), file))
    {

        int fuel = atoi(line);
        while (1)
        {
            fuel = fuel / 3 - 2;
            if (fuel >= 0)
            {
                total += fuel;
            }
            else
            {
                break;
            }
        }
    }
    printf("%d \n", total);
}

int main()
{
    FILE *file = fopen("./input.txt", "r");
    if (file == NULL)
        exit(1);
    partOne(file);
    rewind(file);
    partTwo(file);
    fclose(file);
}