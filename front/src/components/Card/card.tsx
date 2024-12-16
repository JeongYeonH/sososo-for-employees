import { DefaultCardImage } from 'assets/images/card-image-resource';
import React, { forwardRef } from 'react';
import { IoEye, IoPerson } from "react-icons/io5";
import './card.css'

interface Props {
    clubTitle: string,
    clubCurrentMemberNum: number,
    clubPageVisitedNum: number,
    clubCardImage: string,
    clubCardLocation: string,
}


const CardBox = forwardRef<HTMLInputElement, Props>((props: Props ,ref) => {   

    return (
        <div className='card-container' >
            <div className='default-card-image'>
                <img className="default-image" src = {props.clubCardImage} alt="DefaultCardImage" />
            </div>
            <div className='card-info'>
                <div className='card-title-container'>
                    <h1 className='card-title'>{props.clubTitle}</h1>
                    <div className='card-title-sub-info'>{props.clubCardLocation}</div>
                </div>
                <div className='card-sub-info-container'>
                    <div className='card-memeber-num'>
                        <IoPerson />
                        <li>{props.clubCurrentMemberNum}</li> 
                    </div>
                    <div className='card-viewed'>
                        < IoEye />
                        <li>{props.clubPageVisitedNum}</li>                      
                    </div>
                </div>
            </div>
        </div>
    );
});


export default CardBox;