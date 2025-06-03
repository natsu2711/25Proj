//const http = require('http');
const express =require('express');
const app =express();
const port = 3000;

/*const server =http.createServer((request,Response)=>{
    Response.write('hello~~');
    Response.end();

});

server.listen(3000,()=>{
    console.log('服务已启动！！！');
})
*/

//使用JSON中间件
app.use(express.json());


app.listen(port,() =>{
    console.log('服务已启动！！！');
});


app.get('/',(request,response) =>{
    response.send('来咯');
});

//准备一组数据
const data=[
    {
        id: 1,
        title: '关山月',
        content : '明月出1山，苍茫云海间'
    },
        {
        id: 2,
        title: '关2月',
        content : '明月出2山，苍茫云海间'
    },
        {
        id: 3,
        title: '关3月',
        content : '明月出3山，苍茫云海间'
    }
];

//要给客户端需要转换成json格式，做出响应时需要定义头部信息
app.get('/posts',(request,response) =>{
    response.send(data);

})

//定义接口的时候可以定义一些参数，接口处理器（就是那个()=>函数），会根据参数的值决定把什么数据发给客户端
app.get('/posts/:postId',(request,response) =>{
    //获取内容ID
    const{ postId } = request.params;

    //查找具体内容
    const posts = data.filter(item => item.id == postId);

    //做出响应
    response.send(posts[0]);

})

//创建内容
app.post('/posts',(request,response) =>{
    //获取请求里的数据
    const { content } = request.body;

    //设置响应状态码
    response.status(201);

    //输出请求头部数据
    console.log(request.headers['sing-along']);

    //设置响应的头部数据
    response.set('Sing-Along','How I wonder what you are!')

    //做出响应
    response.send({
        message : `成功创建了内容： ${content}`
    });
});