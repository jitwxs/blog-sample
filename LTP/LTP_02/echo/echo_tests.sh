#!/bin/sh
#
# Author : jitwxs <jitwxs@foxmail.com>
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
# the GNU General Public License for more details.
#
# Test echo command with some basic options.
#

TST_CNT=4
TST_TESTFUNC=do_test
TST_NEEDS_TMPDIR=1
TST_NEEDS_CMDS="echo"
. tst_test.sh

echo_test()
{
	local echo_opt=$1
	local echo_content=$2

	local echo_cmd="echo $echo_opt $echo_content"

	$echo_cmd > temp 2>&1
	if [ $? -ne 0 ]; then
		grep -q -E "unknown option|invalid option" temp
		if [ $? -eq 0 ]; then
			tst_res TCONF "$echo_cmd not supported."
		else
			tst_res TFAIL "$echo_cmd failed."
		fi
		return
	fi

	line=`wc -l temp | awk '{print $1}'`

	if [ -z $echo_opt ];then
		if [ $line -ne 1 ];then
			tst_res TFAIL "$echo_cmd failed."
			return
		fi
	else
		if [ $echo_opt = "-e" ];then
			if [ $line -ne 2 ];then
				tst_res TFAIL "$echo_cmd failed."
				return
			fi
		fi
	fi

	tst_res TPASS "echo passed with $echo_opt option."
}

do_test()
{
	case $1 in
	1) echo_test "" "helloo\nworld";;
	2) echo_test "-e" "helloo\nworld";;
	3) echo_test "--help";;
	4) echo_test "--version";;
	esac
}

tst_run
