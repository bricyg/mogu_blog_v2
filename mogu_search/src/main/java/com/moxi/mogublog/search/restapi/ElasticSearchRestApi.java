//package com.moxi.mogublog.search.restapi;
//
//import com.moxi.mogublog.search.client.BlogClient;
//import com.moxi.mogublog.search.global.MessageConf;
//import com.moxi.mogublog.search.global.SysConf;
//import com.moxi.mogublog.search.pojo.ESBlogIndex;
//import com.moxi.mogublog.search.reposlitory.BlogRepository;
//import com.moxi.mogublog.search.service.ElasticSearchService;
//import com.moxi.mogublog.utils.ResultUtil;
//import com.moxi.mogublog.utils.StringUtils;
//import com.moxi.mogublog.utils.WebUtils;
//import com.moxi.mogublog.xo.entity.Blog;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import java.text.ParseException;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * ElasticSearch RestAPI
// *
// * @author: 陌溪
// * @create: 2020年1月15日16:26:16
// */
//@RequestMapping("/search")
//@Api(value = "ElasticSearchRestApi", tags = {"ElasticSearchRestApi"})
//@RestController
//public class ElasticSearchRestApi {
//
//    @Autowired
//    private ElasticSearchService searchService;
//
//    @Autowired
//    private BlogRepository blogRepository;
//
//    @Autowired
//    private ElasticsearchTemplate elasticsearchTemplate;
//
//    @Autowired
//    private BlogClient blogClient;
//
//
//    @ApiOperation(value = "通过ElasticSearch搜索博客", notes = "通过ElasticSearch搜索博客", response = String.class)
//    @GetMapping("/elasticSearchBlog")
//    public String searchBlog(HttpServletRequest request,
//                             @RequestParam(required = false) String keywords,
//                             @RequestParam(name = "currentPage", required = false, defaultValue = "1") Integer currentPage,
//                             @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
//
//        if (StringUtils.isEmpty(keywords)) {
//            return ResultUtil.result(SysConf.ERROR, MessageConf.KEYWORD_IS_NOT_EMPTY);
//        }
//        return ResultUtil.result(SysConf.SUCCESS, searchService.search(keywords, currentPage, pageSize));
//    }
//
//    @ApiOperation(value = "通过uids删除ElasticSearch博客索引", notes = "通过uids删除ElasticSearch博客索引", response = String.class)
//    @PostMapping("/deleteElasticSearchByUids")
//    public String deleteElasticSearchByUids(@RequestParam(required = true) String uids) {
//
//        List<String> uidList = StringUtils.changeStringToString(uids, SysConf.FILE_SEGMENTATION);
//
//        for(String uid : uidList) {
//            blogRepository.deleteById(uid);
//        }
//
//        return ResultUtil.result(SysConf.SUCCESS, MessageConf.DELETE_SUCCESS);
//    }
//
//    @ApiOperation(value = "通过博客uid删除ElasticSearch博客索引", notes = "通过uid删除博客", response = String.class)
//    @PostMapping("/deleteElasticSearchByUid")
//    public String deleteElasticSearchByUid(@RequestParam(required = true) String uid) {
//        blogRepository.deleteById(uid);
//        return ResultUtil.result(SysConf.SUCCESS, MessageConf.DELETE_SUCCESS);
//    }
//
//    @ApiOperation(value = "ElasticSearch通过博客Uid添加索引", notes = "添加博客", response = String.class)
//    @PostMapping("/addElasticSearchIndexByUid")
//    public String addElasticSearchIndexByUid(@RequestParam(required = true) String uid) {
//
//        String result = blogClient.getBlogByUid(uid);
//
//        Blog eblog = WebUtils.getData(result, Blog.class);
//        if(eblog == null) {
//            return ResultUtil.result(SysConf.ERROR, MessageConf.INSERT_FAIL);
//        }
//        ESBlogIndex blog = searchService.buidBlog(eblog);
//        blogRepository.save(blog);
//        return ResultUtil.result(SysConf.SUCCESS, MessageConf.INSERT_SUCCESS);
//    }
//
//    @ApiOperation(value = "ElasticSearch初始化索引", notes = "ElasticSearch初始化索引", response = String.class)
//    @PostMapping("/initElasticSearchIndex")
//    public String initElasticSearchIndex() throws ParseException {
//        elasticsearchTemplate.deleteIndex(ESBlogIndex.class);
//        elasticsearchTemplate.createIndex(ESBlogIndex.class);
//        elasticsearchTemplate.putMapping(ESBlogIndex.class);
//
//        Long page = 1L;
//        Long row = 10L;
//        Integer size = 0;
//
//        do {
////            // 查询blog信息
////            String result = blogClient.getNewBlog(page, row);
////            //构建blog
////
////            Map<String, Object> blogMap = (Map<String, Object>) JsonUtils.jsonToObject(result, Map.class);
////            if ("success".equals(blogMap.get("code"))) {
////                Map<String, Object> blogData = (Map<String, Object>) blogMap.get("data");
////                List<Map<String, Object>> blogRecords = (List<Map<String, Object>>) blogData.get("records");
////                size = blogRecords.size();
////                List<com.moxi.mogublog.xo.entity.Blog> EBlogList = new ArrayList<>();
////                for (int i = 0; i < size; i++) {
////
////                    if (org.springframework.util.StringUtils.isEmpty(blogRecords.get(i).get("uid"))) {
////                        continue;
////                    }
////
////                    List<Map<String, Object>> tagList = (List<Map<String, Object>>) blogRecords.get(i).get("tagList");
////                    Map<String, Object> MapBlogSort = (Map<String, Object>) blogRecords.get(i).get("blogSort");
////                    List photoList = (List) blogRecords.get(i).get("photoList");
////                    com.moxi.mogublog.xo.entity.Blog EBlog = new com.moxi.mogublog.xo.entity.Blog();
////                    BlogSort blogSort = new BlogSort();
////                    blogSort.setSortName(MapBlogSort.get("sortName").toString());
////                    blogSort.setContent(MapBlogSort.get("content").toString());
////                    blogSort.setUid(MapBlogSort.get("uid").toString());
////                    EBlog.setUid((String) blogRecords.get(i).get("uid"));
////                    EBlog.setTitle((String) blogRecords.get(i).get("title"));
////                    EBlog.setSummary((String) blogRecords.get(i).get("summary"));
////                    EBlog.setBlogSortUid(blogSort.getUid());
////                    EBlog.setBlogSort(blogSort);
////                    EBlog.setIsPublish((String) blogRecords.get(i).get("isPublish"));
////                    EBlog.setAuthor((String) blogRecords.get(i).get("author"));
////                    Date createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(blogRecords.get(i).get("createTime").toString());
////                    EBlog.setCreateTime(createTime);
////                    EBlog.setPhotoList(photoList);
////                    EBlogList.add(EBlog);
////
////                }
////                List<ESBlogIndex> blogList = EBlogList.stream()
////                        .map(searchService::buidBlog).collect(Collectors.toList());
////                //存入索引库
////                blogRepository.saveAll(blogList);
////                //翻页
////                page++;
////            }
//
//            // 查询blog信息
//            String result = blogClient.getNewBlog(page, row);
//
//            //构建blog
//            List<Blog> blogList = WebUtils.getList(result, Blog.class);
//            size = blogList.size();
//
//            List<ESBlogIndex> eSBlogIndexList = blogList.stream()
//                        .map(searchService::buidBlog).collect(Collectors.toList());
//            //存入索引库
//            blogRepository.saveAll(eSBlogIndexList);
//            // 翻页
//            page++;
//        } while (size == 15);
//
//        return ResultUtil.result(SysConf.SUCCESS, MessageConf.OPERATION_SUCCESS);
//    }
//}