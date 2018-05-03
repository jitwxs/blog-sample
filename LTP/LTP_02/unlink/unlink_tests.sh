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
# Test unlink command with some basic options.
#

TST_CNT=3
TST_SETUP=setup
TST_TESTFUNC=do_test
TST_NEEDS_TMPDIR=1
. tst_test.sh

setup()
{
	ROD touch "demo"
}

unlink_test(){
	local unlink_opt=$1
	local unlink_file=$2

	unlink_cmd="unlink $unlink_opt $unlink_file"

	if [ -z $unlink_file ];then
		eval "$unlink_cmd" > temp 2>&1
		if [ $? -ne 0 ];then
			grep -q -E "unknown option|invalid option" temp
			if [ $? -eq 0 ];then
				tst_res TCONF "$unlink_cmd not supposted."
			else
				tst_res TFAIL "$unlink_cmd option failed."
			fi
			return
		fi
	else
		`unlink $unlink_file`
		if [ -f $unlink_file ];then
			tst_res TFAIL "$unlink_cmd failed."
			return
		fi
	fi

	tst_res TPASS "unlink passed with $unlink_opt option."

}

do_test(){
case $1 in
    1) unlink_test "" "demo" ;;
    2) unlink_test --help ;;
    3) unlink_test --version ;;
    esac
}

tst_run
