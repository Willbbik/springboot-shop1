# springboot-shop1[![Build Status](https://app.travis-ci.com/Willbbik/springboot-shop1.svg?token=1bwEC37snuxQCAFWRAxt&branch=master)](https://app.travis-ci.com/Willbbik/springboot-shop1)

<p>처음으로 진행해본 미니 쇼핑몰 개인 프로젝트입니다.</p>
<br/>

<h1>배포환경</h1>
<div>
    <ul>
        <li>AWS (비용 문제로 배포 중단)</li>
    </ul>
</div>
<br/>
<h1>사용한 기술스택</h1>
<div>
    <ul>
        <li>JAVA11</li>
        <li>Spring Boot</li>
        <li>Spring Security</li>
        <li>Spring Data Jpa</li>
        <li>Querydsl</li>
        <li>Thymeleaf</li>
        <li>Mysql</li>
        <li>Redis</li>
    </ul>
</div>
<br/>
<br/>
<h1>구현 기능</h1>
<div>
    <ul>
        <li>
            <strong>계정 관련</strong>
            <ul>
                <li>로그인,로그아웃,회원가입,회원탈퇴, OAuth2 로그인(카카오)</li>
                <li>ID찾기</li>
                <li>회원가입시 문자 인증</li>
                <li>MyPage(주문 목록, 작성한 QnA목록)</li>
                <li>관리자 페이지를 통한 상품 조회,배송,주문관리</li>
            </ul>
        </li>
        <li>
            <strong>게시판</strong>
            <ul>
                <li>게시글 CRUD</li>
                <li>게시글 공개,비공개 설정</li>
                <li>비동기 댓글, 대댓글 CRUD 및 공개,비공개 설정</li>
                <li>버튼 페이징</li>
            </ul>
        </li>
        <li>
            <strong>상품</strong>
            <ul>
                <li>상품 검색</li>
                <li>카테고리별 상품 조회</li>
                <li>noOffset 페이징</li>
                <li>상품 등록,삭제</li>
                <li>최신순,오래된순 정렬</li>
                <li>리뷰, QnA, QnA 관리자 답글</li>
            </ul>
        </li>
        <li>
            <strong>장바구니</strong>
            <ul>
                <li>추가, 수량변경, 개별주문, 선택주문</li>
            </ul>
        </li>
        <li>
            <strong>결제</strong>
            <ul>
                <li>카카오페이, 가상계좌</li>
            </ul>
        </li>
    </ul>
</div>
<br/>
<br/>
<h1>이미지</h1>
<p>( 이미지는 aws s3에 저장하고 사용하는데 비용문제로 사용 중단하면서 이미지는 없습니다 )</p>
<br/>
<div>
    <ul>
        <li>
            <strong>메인 페이지</strong>
            <ul>
                <li>카테고리별 상품 조회 가능</li>
                <li>더보기 버튼 (noOffset 페이징)</li>
                <li>
                    <img src="https://user-images.githubusercontent.com/89326946/150129402-868062cc-952d-4261-a3a9-a1ea6f9a4611.png">
                </li>
            </ul>
        </li>
        <br/>
        <li>
            <strong>회원가입</strong>
            <ul>
                <li>문자인증 기능, 아이디 중복검사</li>
                <li>프론트, 서버 모두 유효성 검사</li>
                <li>
                    <img src="https://user-images.githubusercontent.com/89326946/150129225-9668ed2b-21ea-4f2d-87f5-d25ceeda701f.png ">
                </li>
            </ul>
        </li>
        <br/>
        <li>
            <strong>로그인</strong>
            <ul>
                <li>카카오 로그인 구현</li>
                <li>프론트, 서버 모두 유효성 검사</li>
                <li>
                    <img src="https://user-images.githubusercontent.com/89326946/150129246-d77023e6-d76b-4950-af5b-b0f1523dd198.png">
                </li>
            </ul>
        </li>
        <br/>
        <li>
            <strong>MyPage</strong>
            <ul>
                <li>주문, QnA버튼 클릭시 비동기로 내용 가져오게끔 구현</li>
                <li>회원탈퇴 기능</li>
                <li>주문상세보기 클릭시 해당 주문 관련 정보들 표시</li>
                <li>더보기 버튼 (noOffset 페이징)</li>
                <li>
                    <img src="https://user-images.githubusercontent.com/89326946/150129415-ed601095-fe4d-4548-8f83-b7b178f888bf.png">
                </li>
            </ul>
        </li>
        <br/>
        <li>
            <strong>자유게시판</strong>
            <ul>
                <li>기본적인 CRUD</li>
                <li>댓글, 대댓글 CRUD, 비공개 여부 설정가능</li>
                <li>작성자 아이디 마스킹처리</li>
                <li>페이징</li>
                <li>
                    <img src="https://user-images.githubusercontent.com/89326946/150129443-4f43da01-8ed8-450f-87b6-c6c994d6e3d4.png">
                </li>
                <li>
                    <img src="https://user-images.githubusercontent.com/89326946/150262071-b6dbc5ee-1fb8-46b5-85bc-457b2a449e39.png">
                </li>
            </ul>
        </li>
        <br/>
        <br/>
        <li>
            <strong>상품, 상품리뷰, 상품 QnA</strong>
            <ul>
                <li>수량 변경 가능</li>
                <li>리뷰 : 상품 구매시에만 리뷰 작성 가능, 정렬 기능</li>
                <li>QnA : 비공개여부 선택 가능, 관리자만 답글 가능</li>
                <li>일반 페이징, noOffset 페이징</li>
                <li>
                    <img src="https://user-images.githubusercontent.com/89326946/150262699-51323395-5c2b-49a0-b31c-df5ce1deffaf.png">
                </li>
                <li>
                    <img src="https://user-images.githubusercontent.com/89326946/150261169-4ba3f6dc-fee5-4d9c-b6d9-e4d894225d3f.png">
                </li>
                <li>
                    <img src="https://user-images.githubusercontent.com/89326946/150261150-4780b106-e5df-49d8-89a4-d5f920f0b506.png">
                </li>
            </ul>
        </li>
        <br/>
        <br/>
        <li>
            <strong>장바구니</strong>
            <ul>
                <li>개별 혹은 선택주문</li>
                <li>비동기 수량변경, 삭제 가능</li>
                <li>
                    <img src="https://user-images.githubusercontent.com/89326946/150262474-ad4f9593-b81f-4131-9749-f2701bcb4187.png">
                </li>
              
            </ul>
        </li>
        <br/>
        <li>
            <strong>주문 정보</strong>
            <ul>
                <li>주문한 상품에 대한 기본 정보들</li>
                <li>가상계좌 구매시 유효기간과, 계좌번호도 표시</li>
                <li>
                    <img src="https://user-images.githubusercontent.com/89326946/150129429-b2760c68-eafb-44d6-bdd2-6d1e8a0ad765.png">
                </li>
                <li>
                    <img src="https://user-images.githubusercontent.com/89326946/150158308-deb2e458-4b20-465f-8330-83c022423d44.png">
                </li>
            </ul>
        </li>
        <br/>
        <li>
            <strong>상품 주문</strong>
            <ul>
                <li>구매하려는 상품 목록 표시</li>
                <li>구매 클릭시 서버에서 배송정보 유효성검사후 결제 진행</li>
                <li>
                    <img src="https://user-images.githubusercontent.com/89326946/150129455-b47a3d7c-f46b-4a46-b11c-859321ca96d8.png">
                </li>
            </ul>
        </li>
        <br/>
        <li>
            <strong>관리자 페이지 메인</strong>
            <ul>
                <li>최근 가입한 유저목록, 주문 목록 조회</li>
                <li>
                    <img src="https://user-images.githubusercontent.com/89326946/150129475-eaef4772-64d6-42bb-9f7f-9cd55950756b.png">
                </li>
            </ul>
        </li>
        <br/>
        <li>
            <strong>관리자 페이지 주문목록</strong>
            <ul>
                <li>주문상태 변경 기능</li>
                <li>동적 검색</li>
                <li>
                    <img src="https://user-images.githubusercontent.com/89326946/150129493-4f176fcb-e4a3-4b90-a39f-5d5a3a83a6d6.png">
                </li>
            </ul>
        </li>
        <br/>
        <li>
            <strong>관리자 페이지 상품배송</strong>
            <ul>
                <li>입금 완료된 주문만 표시</li>
                <li>운송장 입력후 배송시 해당 주문 배송상태 변경</li>
                <li>
                    <img src="https://user-images.githubusercontent.com/89326946/150158645-8d72a797-4af7-481a-b693-fb5a9e8288af.png">
                </li>
            </ul>
        </li>
        <br/>
        <li>
            <strong>관리자 페이지 상품 목록</strong>
            <ul>
                <li>상품 동적 검색 및 삭제</li>
                <li>
                    <img src="https://user-images.githubusercontent.com/89326946/150129486-ac9fd377-3744-4b43-8878-b37a6c580b72.png">
                </li>
            </ul>
        </li>
    </ul>
</div>
<br/>
<br/>
<div>
    <strong>개인 블로그 주소</strong>
    <ul>
        <li><a href="https://clear-clouds.tistory.com">블로그</a></li>
    </ul>
</div>

