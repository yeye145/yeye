#include <stdio.h>



void QuickSort(int a[], int left, int right) {
	
    // �����߽糬Խ�ұ߽磬��ֹ
    if (left > right) {
       return;
    }

    int start = left;
    int end = right;
    int baseNum = a[start];
    while (start != end) {

        // �����߽�С���ұ߽磬��startָ������ִ��ڻ�׼�����ƶ��ұ߽�end
        while ( (end > start) && (a[end] >= baseNum) ) {
            end--;
        }

        // �����߽�С���ұ߽磬��startָ�������С�ڻ�׼�����ƶ���߽�start
        while ( (start < end) && (a[start] <= baseNum) ) {
            start++;
        }

        // ��������������
        int temp = a[start];
        a[start] = a[end];
        a[end] = temp;

    }

    // start��end�غ�ʱ�������ǹ�ָͬ�����������߽绥��
    a[left] = a[start];
    a[start] = baseNum;

    // �ݹ����
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
    
