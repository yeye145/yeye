#include <stdio.h>
#include <stdlib.h>

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




 main() {
	
    int a[] = {4, 2, 2, 8, 3, 3, 1, 5};
    int n = sizeof(a) / sizeof(a[0]);


    CountSort(a, n);

    for (int i = 0; i < n; i++) {
        printf("%d ", a[i]);
    }


}





