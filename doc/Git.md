# Git技巧

## 给Github添加sshkey

Terminal中，输入命令 ssh-keygen 一路回车会生成id_rsa.pub文件，再使用命令cat id_rsa.pub > pbcopy
即可复制sshkey。

打开github账户设置界面，点击添加sshkey，复制既可。

## 命令模式创建远程github仓库


    //创建远程仓库命令
    curl -u 'username' https://api.github.com/user/repos -d '{"name":"reponame"}'

    //username为用户的名称， reponame为仓库名称

    //添加远程仓库
    git remote add origin git@github.com:username/RepoName.git
    git push origin master

    //username为github用户名
    //RepoName.git为创建的仓库名称

