#!/usr/bin/env python
# -*- coding: utf-8 -*-
import random
import time

class WebLogGeneration(object):
    def __init__(self):
        self.user_agent_dist = {
            0.0 : "Mozilla / 5.0(WindowsNT6.1;WOW64) AppleWebKit / 537.36(KHTML, likeGecko) Chrome / 53.0.2785.143Safari / 537.36",
            0.1 : "Mozilla / 5.0(WindowsNT6.1;WOW64) AppleWebKit / Safari / 537.36",
            0.2 : "Mozilla / 5.0(WindowsNT6.1;WOW64) AppleWebKit / 537.36(KHTML, likeGecko) Chrome / 53.0.2785.143Safari / 537.36",
            0.3 : "Mozilla / 6.0(WindowsNT6.1;WOW64) AppleWebKit / Safari / 537.36",
            0.4 : "Mozilla / 5.0(Android) AppleWebKit / 537.36(KHTML, likeGecko) Chrome / 53.0.2785.143Safari / 537.36",
            0.5 : "Mozilla / 5.0(Iphone) AppleWebKit / Safari / 537.36",
            0.6 : "Mozilla / 4.0(MAC) AppleWebKit / 537.36(KHTML, likeGecko) Chrome / 53.0.2785.143Safari / 537.36",
            0.7 : "Mozilla / 5.0(Linux) AppleWebKit / Safari / 537.36",
            0.8 : "Mozilla / 4.0(Linux) AppleWebKit / 537.36(KHTML, likeGecko) Chrome / 53.0.2785.143Safari / 537.36",
            0.9 : "Mozilla / 6.0(Linux) AppleWebKit / Safari / 537.36",
            1 : " "

        }

        self.ip_slice_list = [10,29,300,46,55,63,72,87,98,132,156,124,167,143,187,168,190,201,202,214,215,222]

        self.url_path_list = ["login.php","list.php","adin.action","index.html","zhkmxx.jsp"]

        self.http_refer = [
            "http://www.baidu.com/s?wd={query}",
            "http://www.google.cn/s?search?q={query}",
            "http://www.sogou.com/web?query={query}",
            "http://www.yahoo.com/s?p={query}",
            "http://cn.bing.com/search?1={query}"
        ]
        self.search_keyword = ["spark","news","hive","hello","popup"]

    def sample_ip(self):
        slice = random.sample(self.ip_slice_list,4) #随机取四个拼ip
        return ".".join([str(item) for item in slice])

    def sample_url(self):
        return random.sample(self.url_path_list,1)[0]

    def sample_user_agent(self):
        dist_upon =random.uniform(0,1)
        return self.user_agent_dist[float('%0.1f' % dist_upon)]

    def sample_refer(self):
        if random.uniform(0,1) > 0.2: #Setting 20% pv have refer option
            return "-"
        refer_str = random.sample(self.http_refer,1)
        query_str = random.sample(self.search_keyword,1)
        return refer_str[0].format(query=query_str[0])

    def sample_one_log(self, count = 3):
        time_str = time.strftime("%Y-%m-%d %H:%M:%S",time.localtime())
        while count > 1:
            query_log = "{ip} - - [{localtime}] \"GET /{url} HTTP/1.1\" 200 0 \"{refer}\" \"{user_agent}\" \"-\""\
                .format(ip=self.sample_ip(),
                        localtime=time_str,
                        url=self.sample_url(),
                        user_agent = self.sample_user_agent(),
                        refer=self.sample_refer()
                        )

            print query_log
            count = count - 1


if __name__ == '__main__':
    web_log_gene = WebLogGeneration()
    web_log_gene.sample_one_log(random.uniform(10000,30000))
