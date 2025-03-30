#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "sort.h"

#define SIZE 100
#define ITERS 100000



void generate_small_array(int *arr) {
	
    for (int i = 0; i < SIZE; i++) 
		arr[i] = rand() % 1000;
		
}



void test_small(const char *name, void (*sort)(int*, int), int *src) {
	
    int *temp = malloc(SIZE * sizeof(int));
    clock_t start = clock();
    
    for (int i = 0; i < ITERS; i++) {
        for (int j = 0; j < SIZE; j++) temp[j] = src[j];
        sort(temp, SIZE);
    }
    
    clock_t end = clock();
    
    printf("%-15s: %.6f s\n", name, (double)(end - start) / CLOCKS_PER_SEC);
    free(temp);
}



void MergeSortWrapper(int *arr, int n) { 
	MergeSort(arr, 0, n-1); 
}



void QuickSortWrapper(int *arr, int n) { 
	QuickSort(arr, 0, n-1); 
}



int main() {
    srand(time(NULL));
    int *arr = malloc(SIZE * sizeof(int));
    generate_small_array(arr);

    printf("Testing %d sorts of size %d:\n", ITERS, SIZE);
    test_small("InsertSort", InsertSort, arr);
    test_small("MergeSort", MergeSortWrapper, arr);
    test_small("QuickSort", QuickSortWrapper, arr);
    test_small("CountSort", CountSort, arr);
    test_small("RadixSort", RadixCountSort, arr);

    free(arr);
    return 0;
}
