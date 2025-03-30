#include <stdio.h>
#include <stdlib.h>


// 获取数组中的最大值，找出排序的最大位数 
int getMax(int a[], int n) {
	
    int max = a[0];
    
    for (int i = 1; i < n; i++) {
        if (a[i] > max) {
            max = a[i];
        }
    }
    
    return max;
}

// 按指定位数进行计数排序
void countSort(int a[], int len, int exp) {
	
    const int BASE = 10; // 容量为10的小桶 
   
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




void RadixCountSort(int a[], int len) {
	
    if (len <= 0) return;

    // 获取最大值以确定位数
    int max = getMax(a, len);

    // 从个位到最高位依次排序
    for (int i = 1; max / i > 0; i *= 10) {
        countSort(a, len, i);
    }
    
}




main() {
    
    int a[] = {236, 415, 328, 180, 324, 454, 895, 856, 637, 694};
    int a_len = sizeof(a) / sizeof(a[0]);
    

    RadixCountSort(a, a_len);
    
	 for (int i = 0; i < a_len; i++) {
        printf("%d ", a[i]);
    }

}







