import { Injectable } from '@angular/core';
import {HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { retry, catchError, finalize } from 'rxjs/operators';


@Injectable()
export class CustomHttpInterceptor implements HttpInterceptor {

    
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
       
        const copiedReq = req.clone({
                withCredentials: true
        });

        return next.handle(copiedReq).pipe(
        //    retry(1),
            catchError((error:HttpErrorResponse)=>{
                alert(`HTTP Error: ${copiedReq.url}`);
                return throwError(error);
            }),
            finalize(() => {
                const profilinMsg =`${copiedReq.method}"${copiedReq.urlWithParams}"`;
                console.log(profilinMsg);
            })
        )
    }
    

}