#include <stdio.h>

void InsertSort(int arr[], int n) {
	
    int i, j, key;
    
    for (i = 1; i < n; i++) {
    	
    	// ȡ����ǰ������Ԫ��
        key = arr[i];
        j = i - 1;

        // ������key��Ԫ�غ��� 
        while (j >= 0 && arr[j] > key) {
            arr[j + 1] = arr[j];
            j--;
        }
        
        // ���뵽��ȷλ��
        arr[j + 1] = key;
    }
}

main() {
	
    int a[] = {9, 5, 1, 4, 3, 2, 8, 6, 7, 0};
    
    int a_len = sizeof(a) / sizeof(a[0]);


    InsertSort(a, a_len);
    
    for (int i = 0; i < a_len; i++) {
        printf("%d  ", a[i]);
    }

}
