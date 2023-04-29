import http from '@/utils/request.js'
export function policy() {
   return  new Promise((resolve,reject)=>{
    http({
            url: "/third-party/oss/policy",
            method: "get",
        }).then(({ data }) => {
            resolve(data);
        })
    });
}
