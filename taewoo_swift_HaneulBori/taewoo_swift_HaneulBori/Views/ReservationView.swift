//
//  ReservationView.swift
//  taewoo_swift_HaneulBori
//
//  Created by 권태우 on 2021/07/28.
//

import SwiftUI

struct ReservationView: View {
    @State var time = Date()
    @State var isReserved = false
    
    var body: some View {
        VStack {
            Text("세탁기 예약")
                .font(.title)
            Spacer()
            DatePicker(selection: $time, displayedComponents: .hourAndMinute){
                Text("예약 시간")
//                    .frame(width: 100.0)
            }
            .padding()
            .frame(width: 380.0)
//            .datePickerStyle(WheelDatePickerStyle())
            Button(action: {isReserved = true}){    // Female
                if isReserved{
                    Text("예약하기")
                        .foregroundColor(Color.white)
                        .padding()
                        .frame(width: 185)
                        .border(Color.black)
                        .background(Color.black)
                        .opacity(0.5)
                }
                else{
                    Text("예약하기")
                        .foregroundColor(Color.white)
                        .padding()
                        .frame(width: 185)
                        .border(Color.black)
                        .background(Color.black)
                }
            }
            .disabled(isReserved)
            Spacer()
        }
    }
}

struct ReservationView_Previews: PreviewProvider {
    static var previews: some View {
        ReservationView()
    }
}