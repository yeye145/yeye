#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int main(int argc, char **argv) {
	
    if (argc != 3) {
        printf("Usage: %s <size> <output_file>\n", argv[0]);
        return 1;
    }
    int n = atoi(argv[1]);
    FILE *fp = fopen(argv[2], "w");
    
    srand(time(NULL));
    for (int i = 0; i < n; i++) fprintf(fp, "%d\n", rand() % 1000000);
    fclose(fp);
    return 0;
    
}
