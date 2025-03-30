#include <stdio.h>
#include <stdlib.h>


#define BASE 10


// 插入排序主函数 
void InsertSort(int arr[], int n) {
	
    int i, j, key;
    
    for (i = 1; i < n; i++) {
    	
    	// 取出当前待插入元素
        key = arr[i];
        j = i - 1;

        // 将大于key的元素后移 
        while (j >= 0 && arr[j] > key) {
            arr[j + 1] = arr[j];
            j--;
        }
        
        // 插入到正确位置
        arr[j + 1] = key;
    }
}



// 归并排序辅助函数——合并两个有序子数组
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



// 快速排序主函数 
void QuickSort(int a[], int left, int right) {
	
    // 如果左边界超越右边界，终止
    if (left > right) {
       return;
    }

    int start = left;
    int end = right;
    int baseNum = a[start];
    while (start != end) {

        // 如果左边界小于右边界，且start指向的数字大于基准数，移动右边界end
        while ( (end > start) && (a[end] >= baseNum) ) {
            end--;
        }

        // 如果左边界小于右边界，且start指向的数字小于基准数，移动左边界start
        while ( (start < end) && (a[start] <= baseNum) ) {
            start++;
        }

        // 交换俩个逆序数
        int temp = a[start];
        a[start] = a[end];
        a[end] = temp;

    }

    // start和end重合时，把它们共同指向的数字与左边界互换
    a[left] = a[start];
    a[start] = baseNum;

    // 递归调用
    QuickSort(a, left, end - 1);
    QuickSort(a, start + 1, right);
        
}



// 计数排序主函数 
void CountSort(int *a, int n) {
	
    if (n <= 0) return;

    // 找出数组中的最大值和最小值
    int max = a[0];
    int min = a[0];
    for (int i = 1; i < n; i++) {
        if (a[i] > max) max = a[i];
        if (a[i] < min) min = a[i];
    }

     
    int range = max - min + 1;

    // 创建并初始化计数数组
    int *count = (int *)calloc(range, sizeof(int));

    // 统计每个元素的出现次数
    for (int i = 0; i < n; i++) {
        count[a[i] - min]++;
    }

    // 求出每个元素在排序后数组的右边界位置
    for (int i = 1; i < range; i++) {
        count[i] += count[i - 1];
    }

    // 创建临时数组存放排序结果
    int *result = (int *)malloc(n * sizeof(int));


    // 反向填充输出数组
    for (int i = n - 1; i >= 0; i--) {
        result[count[a[i] - min] - 1] = a[i];
        count[a[i] - min]--;
    }

    // 将排序结果复制回原数组
    for (int i = 0; i < n; i++) {
        a[i] = result[i];
    }

   
    free(count);
    free(result);
}




// 基数计数排序辅助函数——获取数组中的最大值，找出排序的最大位数 
int getMax(int a[], int n) {
	
    int max = a[0];
    
    for (int i = 1; i < n; i++) {
        if (a[i] > max) {
            max = a[i];
        }
    }
    
    return max;
}



// 基数计数排序辅助函数——按指定位数进行计数排序
void countSort(int a[], int len, int exp) {
	  
    int count[BASE] = {0}; // 初始化计数桶

    // 统计当前位的出现次数，跟计数排序差不多 
    for (int i = 0; i < len; i++) {
    	
        int digit = (a[i] / exp) % BASE;
        
        count[digit]++;
        
    }

     // 求出每个元素在排序后数组的右边界位置
    for (int i = 1; i < BASE; i++) {
        count[i] += count[i - 1];
    }
    
    
    
    // 创建临时数组存放排序结果
    int *result = (int *)malloc(len * sizeof(int));

    // 反向填充到输出数组
    for (int i = len - 1; i >= 0; i--) {
        int digit = (a[i] / exp) % BASE;
        result[count[digit] - 1] = a[i]; 
        count[digit]--;
    }

    // 将排序结果复制回原数组
    for (int i = 0; i < len; i++) {
        a[i] = result[i];
    }

    free(result);
}



// 基数计数排序主函数
void RadixCountSort(int a[], int len) {
	
    if (len <= 0) return;

    // 获取最大值以确定位数
    int max = getMax(a, len);

    // 从个位到最高位依次排序
    for (int i = 1; max / i > 0; i *= 10) {
        countSort(a, len, i);
    }
    
}







