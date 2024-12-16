import { DefaultCardImage } from 'assets/images/card-image-resource';
import React, { forwardRef } from 'react';
import { IoEye, IoPerson } from "react-icons/io5";
import './card.css'

interface Props {
    inviteRequestMember: string

}


const InviteCardBox = forwardRef<HTMLInputElement, Props>((props: Props ,ref) => {   

    return (
        <div className='invite-card-container' >

        </div>
    );
});


export default InviteCardBox;