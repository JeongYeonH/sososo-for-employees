import {FaFacebook, FaGithub, FaInstagram, FaTwitter, FaTwitch} from 'react-icons/fa'
import React from 'react'
import './footer.css'

const sections = [
    {
        title: '기업소개'
    },
    {
        title: '광고문의'
    },
    {
        title: '이용약관'
    },
    {
        title: '개인정보 처리방침'
    },
]

const items = [
    {
        name: 'Facebook',
        icon: FaFacebook,
        Link: 'https://facebook.com/'
    },
    {
        name: 'Instagram',
        icon: FaInstagram,
        Link: 'https://instagram.com/'
    },
    {
        name: 'Twitter',
        icon: FaTwitter,
        Link: 'https://twitter.com/'
    },    
    {
        name: 'Twitch',
        icon: FaTwitch,
        Link: 'https://twitch.com/'
    },
    {
        name: 'Github',
        icon: FaGithub,
        Link: 'https://github.com/'
    },
]

const Footer = () => {
  return (
    <>
    <div className="w-full mt-24 bg-white-900 text-black-300 py-y px-2">
        <div className="max-w-[1240px] mx-auto grid grid-cols-2 md:grid-cols-6
          py-8">
            <div className='font-Playball pt-2 text-3xl'>
                SoSoSo
            </div>
            {
                sections.map((section, index) =>(
                    <div key={index}>
                        <h6 className="font-bold uppercase pt-5 text-xs" key={index}>
                            {section.title}
                        </h6>                                 
                    </div>
                ))
            }
            <div className='flex justify-between sm:w-[200px] pt-5 text-2xl'>
                {
                    items.map((x, index) =>{
                        return <x.icon key={index} className='cursor-pointer text-lg' />
                    })
                }
            </div>
        </div>

        <div className='flex flex-col max-w-[1240px]  py-4 mx-auto justify-between
        sm:flex-row text-center text-gray-500 text-sm mb-8 mt-10'>
            <p>
                2024 Designed by HWANG Jeong Yeon. All rights reserved.
            </p>
            <p className='flex list-none space-x-6'> 
                <li>Blogs</li>
                <li>Networks</li>
                <li>Locations</li>
                <li>Resource</li>
            </p>
        </div>
    </div>
    </>
  )
}

export default Footer;