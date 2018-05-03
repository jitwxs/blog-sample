#测试函数执行次数
TST_CNT=1
#测试用例启动函数
TST_TESTFUNC=do_test
. tst_test.sh

echo_test()
{
	local std_in=$1
	local echo_cmd=$(echo $std_in > a.out)
	local echo_res=$std_in
	local cat_res=$(cat a.out)

	if [ $echo_res = $cat_res ]
	then
		tst_res TPASS "$echo_cmd sucessed"	
	else
		tst_res TFAIL "$echo_cmd failed"
	fi
}

do_test()
{
	echo_test "hello\tworld"
}

tst_run