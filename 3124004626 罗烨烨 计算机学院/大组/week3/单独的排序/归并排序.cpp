#include <stdio.h>
#include <stdlib.h>

// 合并两个有序子数组
void merge(int a[], int left, int mid, int right) {
	
    int i, j, k;
    int n1 = mid - left + 1;
    int n2 = right - mid;

    // 创建临时数组
    int *L = (int *)malloc(n1 * sizeof(int));
    int *R = (int *)malloc(n2 * sizeof(int));

    // 拷贝数据到临时数组
    for (i = 0; i < n1; i++)
        L[i] = a[left + i];
        
    for (j = 0; j < n2; j++)
        R[j] = a[mid + 1 + j];

    
    i = 0;    
    j = 0;    
    k = left; 
    
    // 合并临时数组回原数组
    while (i < n1 && j < n2) {
        if (L[i] <= R[j]) {
            a[k] = L[i];
            i++;
        } else {
            a[k] = R[j];
            j++;
        }
        k++;
    }

    // 拷贝剩余元素
    while (i < n1) {
        a[k] = L[i];
        i++;
        k++;
    }
    while (j < n2) {
        a[k] = R[j];
        j++;
        k++;
    }

    free(L);
    free(R);
}

// 归并排序主函数
void MergeSort(int a[], int left, int right) {
	
    if (left >= right) {
    	return;
    }
    
    int mid = left + (right - left) / 2;

    // 递归调用 
    MergeSort(a, left, mid);
    MergeSort(a, mid + 1, right);

    // 合并已排序的子数组
    merge(a, left, mid, right);
    
}




main() {
	
    int a[] = {2, 1, 3, 5, 9, 7, 8, 6};
    
    int a_len = sizeof(a) / sizeof(a[0]);

    MergeSort(a, 0, a_len - 1);

    for (int i = 0; i < a_len; i++)
        printf("%d ", a[i]);

}
