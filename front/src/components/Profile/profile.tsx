import React from 'react'
import './profile.css'
import { DefaultPersonProfileImage } from 'assets/images/club-page-resource'

export const ClubMemberProfile = () => {
  return (
    <div>
        <div className='club-member-profile-image'>
            <DefaultPersonProfileImage />
        </div>
    </div>
  )
}
