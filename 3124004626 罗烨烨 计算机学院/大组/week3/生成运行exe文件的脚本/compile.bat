@echo off

REM 编译测试大数据排序的程序
gcc -std=c99 test_large.c sort.c -o test_large.exe

REM 编译测试小数据排序的程序
gcc -std=c99 test_small.c sort.c -o test_small.exe

REM 编译数据生成程序
gcc -std=c99 generate_data.c -o generate_data.exe

REM 编译文件排序程序
gcc -std=c99 file_sort.c sort.c -o file_sort.exe

echo 编译完成！按任意键退出...
pause