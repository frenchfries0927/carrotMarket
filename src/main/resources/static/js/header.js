function checkLoginError() {
    // 로그인이 필요한 기능에 대한 에러 확인 로직
    console.log("Checking login error...");
}

window.onload = function() {
    checkLoginError();
};
// 로그인 에러 체크 및 로그인 모달 표시
function checkLoginError() {
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('loginError')) {
        alert('이메일 또는 비밀번호가 잘못되었습니다.');
        $('#loginModal').modal('show'); // 로그인 모달 다시 표시
        document.getElementById('password').value = ''; // 비밀번호 필드 지우기
    }
}

// 로그인 시 위치정보 가져오기
function getLocationAndSubmit() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition((position) => {
            const latitude = position.coords.latitude;
            const longitude = position.coords.longitude;

            document.getElementById('latitude').value = latitude;
            document.getElementById('longitude').value = longitude;

            convertLatLngToAddress(latitude, longitude, (address) => {
                document.getElementById('location').value = address;
                document.getElementById('loginForm').submit();
            });
        }, () => {
            alert("위치 정보를 가져오는 데 실패했습니다.");
        });
    } else {
        alert("브라우저가 위치 정보를 지원하지 않습니다.");
    }
}

// 위도와 경도를 주소로 변환하는 함수
function convertLatLngToAddress(latitude, longitude, callback) {
    const url = `/get-address?latitude=${latitude}&longitude=${longitude}`;

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error("Network response was not ok: " + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            if (data.status === "OK" && data.results[0]) {
                const address = data.results[0].formatted_address;
                callback(address);
            } else {
                alert("주소 변환에 실패했습니다.");
            }
        })
        .catch(() => {
            alert("주소를 가져오는 중 오류가 발생했습니다.");
        });
}

// 카카오 SDK 초기화 및 로그인 함수
function initializeKakao() {
    Kakao.init('YOUR_KAKAO_JAVASCRIPT_KEY'); // 실제 JavaScript 키로 변경
}

function kakaoLogin() {
    Kakao.Auth.login({
        success: function(authObj) {
            Kakao.API.request({
                url: '/v2/user/me',
                success: function(res) {
                    const kakaoAccount = res.kakao_account;

                    if (kakaoAccount.email) {
                        const email = kakaoAccount.email;
                        const nickname = kakaoAccount.profile.nickname;

                        $.ajax({
                            url: '/kakaoLogin',
                            method: 'POST',
                            data: { email: email, username: nickname },
                            success: function(response) {
                                if (response.success) {
                                    window.location.href = '/';
                                } else {
                                    alert('로그인에 실패했습니다.');
                                }
                            },
                            error: function() {
                                alert('로그인 요청 중 오류가 발생했습니다.');
                            }
                        });
                    } else {
                        alert('이메일 정보를 제공받을 수 없습니다. 카카오 계정 설정에서 이메일 정보 제공 동의를 확인해주세요.');
                    }
                },
                fail: function(error) {
                    alert('사용자 정보 요청에 실패했습니다.');
                }
            });
        },
        fail: function(err) {
            alert('카카오 로그인에 실패했습니다.');
        }
    });
}
