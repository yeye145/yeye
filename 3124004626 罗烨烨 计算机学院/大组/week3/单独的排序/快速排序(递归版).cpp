#include <stdio.h>



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
    
    
    
main() {
	
	
	
	int a[] = {6,5,2,4,5,1,3,9,8,7};
	    
	int a_len = sizeof(a) / sizeof(a[0]);
	    
    QuickSort(a, 0, a_len - 1);

    for (int i = 0; i < a_len; i++) {
        printf("%d  ", a[i]);
    }  
    	
    	
     	
}
    
