#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "sort.h"



void generate_random_array(int *arr, int n) {
    for (int i = 0; i < n; i++) {
        arr[i] = rand() % 1000000;
    }
}



void copy_array(int *src, int *dest, int n) {
    for (int i = 0; i < n; i++) 
		dest[i] = src[i];
}



void test_sort(const char *name, void (*sort)(int*, int), int *arr, int n) {
    int *temp = malloc(n * sizeof(int));
    copy_array(arr, temp, n);

    clock_t start = clock();
    sort(temp, n);
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
    int sizes[] = {10000, 50000, 200000};
    for (int i = 0; i < 3; i++) {
        int n = sizes[i];
        int *arr = malloc(n * sizeof(int));
        generate_random_array(arr, n);

        printf("Size: %d\n", n);
        test_sort("InsertSort", InsertSort, arr, n);
        test_sort("MergeSort", MergeSortWrapper, arr, n);
        test_sort("QuickSort", QuickSortWrapper, arr, n);
        test_sort("CountSort", CountSort, arr, n);
        test_sort("RadixSort", RadixCountSort, arr, n);
        free(arr);
        printf("\n");
    }
    return 0;
}
