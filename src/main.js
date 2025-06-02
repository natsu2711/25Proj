const http = require('http');

const server =http.createServer((request,Response)=>{
    Response.write('hello~~');
    Response.end();

});

server.listen(3000,()=>{
    console.log('服务已启动！！！');
})