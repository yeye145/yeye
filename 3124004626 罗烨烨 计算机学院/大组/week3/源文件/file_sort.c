#include <stdio.h>
#include <stdlib.h>
#include "sort.h"

int *read_array(const char *filename, int *n) {
	
    FILE *fp = fopen(filename, "r");
    int *arr = NULL, num, capacity = 0;
    *n = 0;
    
    while (fscanf(fp, "%d", &num) == 1) {
        if (*n >= capacity) {
            capacity = capacity ? capacity * 2 : 1;
            arr = realloc(arr, capacity * sizeof(int));
        }
        arr[(*n)++] = num;
    }
    fclose(fp);
    return arr;
}



void write_array(const char *filename, int *arr, int n) {
    FILE *fp = fopen(filename, "w");
    
    for (int i = 0; i < n; i++) 
		fprintf(fp, "%d\n", arr[i]);
		
    fclose(fp);
}



void MergeSortWrapper(int *arr, int n) { 
	MergeSort(arr, 0, n-1); 
}



int main(int argc, char **argv) {
	
    if (argc != 3) {
        printf("Usage: %s <input> <output>\n", argv[0]);
        return 1;
    }
    
    int n;
    int *arr = read_array(argv[1], &n);
    MergeSortWrapper(arr, n);
    write_array(argv[2], arr, n);
    free(arr);
    return 0;
}
