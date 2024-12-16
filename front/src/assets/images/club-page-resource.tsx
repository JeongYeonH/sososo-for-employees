export const DefaultPersonProfileImage = () => {
    const imageUrl = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FcAToPn%2FbtsKva5vCCt%2FkgdcT5vJjjzqTWmw2rKyP1%2Fimg.png";
  
    return (
      <div>
        <img className="default-image" src={imageUrl} alt="Default Person Profile Image" />
      </div>
    );
  };